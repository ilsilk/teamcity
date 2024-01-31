package com.teamcity.api.generators;

import com.teamcity.api.annotations.Optional;
import com.teamcity.api.annotations.Parameterizable;
import com.teamcity.api.annotations.Random;
import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.models.BaseModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public final class TestDataGenerator {

    private TestDataGenerator() {
    }

    /* Основной метод генерации тестовых данных. Если у поля аннотация Optional, оно пропускается, иначе выбор:
    1) если у поля аннотация Parameterizable, и в метод были переданы параметры, то поочередно (по мере встречи полей с
    это аннотацией) устанавливаются переданные параметры. То есть, если по ходу генерации было пройдено 4 поля с
    аннотацией Parameterizable, но параметров в метод было передано 3, то значения будут установлены только у первых
    трех встретившихся элементов в порядке их передачи в метод. Поэтому также важно следить за порядком полей в @Data классе;
    2) иначе, если у поля аннотация Parameterizable и это строка, оно заполняется рандомными данными;
    3) иначе, если поле - наследник класса BaseModel, то оно генерируется, рекурсивно отправляясь в новый метод generate;
    4) иначе, если поле - List, у которого generic type - наследник класса BaseModel, то оно устанавливается списком
    из одного элемента, который генерируется, рекурсивно отправляясь в новый метод generate.
    Параметр generatedModels передается, когда генерируется несколько сущностей в цикле, и содержит в себе сгенерированные
    на предыдущих шагах сущности. Позволяет при генерации сложной сущности, которая своим полем содержит другую сущность,
    сгенерированную на предыдущем шаге, установить ее, а не генерировать новую. Данная логика применяется только для пунктов 3 и 4.
    Например, если был сгенерирован NewProjectDescription, то передав его параметром generatedModels при генерации BuildType,
    то он будет переиспользоваться при установке поля NewProjectDescription project, вместо генерации нового */
    public static BaseModel generate(Collection<BaseModel> generatedModels, Class<? extends BaseModel> generatorClass, Object... parameters) {
        try {
            var instance = generatorClass.getDeclaredConstructor().newInstance();
            for (var field : generatorClass.getDeclaredFields()) {
                field.setAccessible(true);
                if (!field.isAnnotationPresent(Optional.class)) {
                    var generatedClass = generatedModels.stream().filter(m
                            -> m.getClass().equals(field.getType())).findFirst();
                    if (field.isAnnotationPresent(Parameterizable.class) && parameters.length > 0) {
                        field.set(instance, parameters[0]);
                        parameters = Arrays.copyOfRange(parameters, 1, parameters.length);
                    } else if (field.isAnnotationPresent(Random.class) && String.class.equals(field.getType())) {
                        field.set(instance, RandomData.getString());
                    } else if (BaseModel.class.isAssignableFrom(field.getType())) {
                        var finalParameters = parameters;
                        field.set(instance, generatedClass.orElseGet(() -> generate(
                                generatedModels, field.getType().asSubclass(BaseModel.class), finalParameters)));
                    } else if (List.class.isAssignableFrom(field.getType())) {
                        if (field.getGenericType() instanceof ParameterizedType pt) {
                            var typeClass = (Class<?>) pt.getActualTypeArguments()[0];
                            if (BaseModel.class.isAssignableFrom(typeClass)) {
                                var finalParameters = parameters;
                                field.set(instance, generatedClass.map(List::of).orElseGet(() -> List.of(generate(
                                        generatedModels, typeClass.asSubclass(BaseModel.class), finalParameters))));
                            }
                        }
                    }
                }
                field.setAccessible(false);
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                 | NoSuchMethodException e) {
            throw new IllegalStateException("Cannot generate test data", e);
        }
    }

    // Метод, чтобы сгенерировать одну сущность. Передает пустой параметр generatedModels
    public static BaseModel generate(Class<? extends BaseModel> generatorClass, Object... parameters) {
        return generate(Collections.emptyList(), generatorClass, parameters);
    }

    /* Генерация всех сущностей, у которых указан generatorClass в Endpoint. Делает класс Endpoint единственной точкой
    масштабируемости. Достаточно добавить новую строку только туда, чтобы новый объект начал генерироваться в тестовых данных.
    Перебор идет в обратном порядке тому, что определен EnumMap. Объяснение логики работы EnumMap есть в комментарии к
    TestDataStorage.createdEntitiesMap */
    public static EnumMap<Endpoint, BaseModel> generate() {
        var generatedTestData = new EnumMap<Endpoint, BaseModel>(Endpoint.class);
        Arrays.stream(Endpoint.values()).filter(e -> e.getGeneratorClass() != null).sorted(Comparator.reverseOrder())
                .forEach(endpoint -> generatedTestData.put(endpoint, generate(generatedTestData.values(), endpoint.getGeneratorClass())));
        return generatedTestData;
    }

}
