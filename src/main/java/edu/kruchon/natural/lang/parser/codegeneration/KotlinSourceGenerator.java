package edu.kruchon.natural.lang.parser.codegeneration;

import edu.kruchon.natural.lang.parser.freemarker.TemplateProcessor;
import edu.kruchon.natural.lang.parser.syntax.Parameter;
import edu.kruchon.natural.lang.parser.syntax.Triplet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public enum KotlinSourceGenerator {
    INSTANCE;

    private TemplateProcessor templateProcessor = TemplateProcessor.INSTANCE;
    private KotlinGenerationUtils kotlinGenerationUtils = KotlinGenerationUtils.INSTANCE;

    public List<KotlinSource> generate(List<Triplet> triplets) {
        List<KotlinSource> generatedSources = new ArrayList<>();
        generatedSources.addAll(generateSubjectInterfaces(triplets));
        generatedSources.addAll(generateParameterDataClasses(triplets));
        generatedSources.addAll(generateAutomaticTests(triplets));
        return generatedSources;
    }

    // todo generation of multiple tests
    private List<KotlinSource> generateAutomaticTests(List<Triplet> triplets) {
        List<KotlinFunctionCall> functionCalls = triplets.stream()
                .map(this::generateFunctionCall)
                .collect(Collectors.toList());
        return Collections.singletonList(
                generateAutomaticTest(functionCalls)
        );
    }

    private KotlinFunctionCall generateFunctionCall(Triplet triplet) {
        Parameter object = triplet.getObject();
        KotlinConstructorCall constructorCall = createConstructorCallsRecursively(object, 0);
        return new KotlinFunctionCall(triplet.getSubject().toLowerCase(), triplet.getRelationship(), constructorCall);
    }

    private KotlinConstructorCall createConstructorCallsRecursively(Parameter parameter, int nestedLevel) {
        List<Parameter> childrenParameters = parameter.getChildrenParameters();
        String parameterClassName = kotlinGenerationUtils.firstCharToUpperCase(parameter.getName());
        nestedLevel++;
        if (childrenParameters.isEmpty()) {
            return new KotlinConstructorCall(nestedLevel, parameterClassName, Collections.emptyList(), parameter.getValues());
        } else {
            int finalNestedLevel = nestedLevel;
            List<KotlinConstructorCall> childrenConstructorCalls = childrenParameters
                    .stream()
                    .map(childrenParameter -> createConstructorCallsRecursively(childrenParameter, finalNestedLevel))
                    .collect(Collectors.toList());
            return new KotlinConstructorCall(nestedLevel, parameterClassName, childrenConstructorCalls, Collections.emptyList());
        }
    }

    private KotlinSource generateAutomaticTest(List<KotlinFunctionCall> functionCalls) {
        List<String> subjects = functionCalls.stream()
                .map(KotlinFunctionCall::getContextObject)
                .map(kotlinGenerationUtils::firstCharToUpperCase)
                .distinct()
                .collect(Collectors.toList());
        Map<String, Object> templateParameters = new HashMap<>();
        templateParameters.put("subjects", subjects);
        templateParameters.put("functionCalls", functionCalls);
        String content = templateProcessor.process("Test.kt", templateParameters);
        return new KotlinSource("Test.kt", content);
    }

    private List<KotlinSource> generateParameterDataClasses(List<Triplet> triplets) {
        return triplets
                .stream()
                .flatMap(triplet -> {
                    Set<Parameter> collectedChildrenParameters = new HashSet<>();
                    Parameter object = triplet.getObject();
                    collectedChildrenParameters.add(object);
                    addAllChildrenParameters(collectedChildrenParameters, object);
                    return collectedChildrenParameters.stream();
                })
                .distinct()
                .map(this::generateParameterDataClass)
                .collect(Collectors.toList());
    }

    private void addAllChildrenParameters(Set<Parameter> collectedChildrenParameters, Parameter parameter) {
        List<Parameter> childrenParameters = parameter.getChildrenParameters();
        if (childrenParameters.isEmpty()) {
            return;
        }
        collectedChildrenParameters.addAll(childrenParameters);
        for (Parameter childrenParameter : childrenParameters) {
            addAllChildrenParameters(collectedChildrenParameters, childrenParameter);
        }
    }

    private KotlinSource generateParameterDataClass(Parameter parameter) {
        Map<String, Object> templateParameters = new HashMap<>();
        Set<KotlinClass> fieldClasses = new HashSet<>();
        for (Parameter field : parameter.getChildrenParameters()) {
            fieldClasses.add(new KotlinClass(kotlinGenerationUtils.firstCharToUpperCase(field.getName())));
        }
        KotlinClass parameterClass = new KotlinClass(kotlinGenerationUtils.firstCharToUpperCase(parameter.getName()), fieldClasses);
        templateParameters.put("parameterClass", parameterClass);
        String content = templateProcessor.process("Parameter.kt", templateParameters);
        return new KotlinSource(parameterClass.getName() + ".kt", content);
    }

    private List<KotlinSource> generateSubjectInterfaces(List<Triplet> triplets) {
        HashMap<String, Set<KotlinMethod>> subjectKotlinMethodMappings = new HashMap<>();
        triplets.forEach(
                triplet -> {
                    KotlinMethod kotlinMethod = new KotlinMethod(triplet.getRelationship(), triplet.getObject());
                    String subject = triplet.getSubject();
                    subjectKotlinMethodMappings.putIfAbsent(subject, new HashSet<>());
                    subjectKotlinMethodMappings.get(subject).add(kotlinMethod);
                }
        );
        return subjectKotlinMethodMappings
                .entrySet()
                .stream()
                .map(this::generateSubjectInterface)
                .collect(Collectors.toList());
    }

    private KotlinSource generateSubjectInterface(Map.Entry<String, Set<KotlinMethod>> subjectWithKotlinMethods) {
        String subjectClassName = kotlinGenerationUtils.firstCharToUpperCase(subjectWithKotlinMethods.getKey());
        Set<KotlinMethod> kotlinMethods = subjectWithKotlinMethods.getValue();
        HashMap<String, Object> templateParameters = new HashMap<>();
        templateParameters.put("subjectClass", subjectClassName);
        templateParameters.put("kotlinMethods", kotlinMethods);
        String content = templateProcessor.process("Subject.kt", templateParameters);
        return new KotlinSource(subjectClassName + ".kt", content);
    }
}
