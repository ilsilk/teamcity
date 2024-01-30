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

    public static BaseModel generate(Collection<BaseModel> generatedModels, Class<? extends BaseModel> generatorClass, Object... parameters) {
        try {
            var instance = generatorClass.getDeclaredConstructor().newInstance();
            for (var field : generatorClass.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Optional.class)) {
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

    public static BaseModel generate(Class<? extends BaseModel> generatorClass, Object... parameters) {
        return generate(Collections.emptyList(), generatorClass, parameters);
    }

    public static EnumMap<Endpoint, BaseModel> generate() {
        var generatedTestData = new EnumMap<Endpoint, BaseModel>(Endpoint.class);
        Arrays.stream(Endpoint.values()).filter(e -> e.getGeneratorClass() != null).sorted(Comparator.reverseOrder())
                .forEach(endpoint -> generatedTestData.put(endpoint, generate(generatedTestData.values(), endpoint.getGeneratorClass())));
        return generatedTestData;
    }

}
