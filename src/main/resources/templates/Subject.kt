interface ${subjectClass} {
    <#list kotlinMethods as kotlinMethod>
    fun ${kotlinMethod.name}(${kotlinMethod.parameterClassName} ${kotlinMethod.parameterName})
    </#list>
}