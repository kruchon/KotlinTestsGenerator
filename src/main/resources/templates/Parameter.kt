data class ${parameterClass.name} (
    <#list parameterClass.fields as field>
    val ${field.name?lower_case}: ${field.name}
    <#else>
    val values: Array<String>
    </#list>
)