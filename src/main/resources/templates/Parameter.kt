<#list parameterClass.fields as field>import ${generationPackage}.${field.name}<#if field?has_next>${'\n'}</#if></#list><#if (parameterClass.fields?size > 0)>${'\n'}</#if>data class ${parameterClass.name} (
    <#list parameterClass.fields as field>val ${field.name?lower_case}: ${field.name}</#list><#if (parameterClass.valueTypes?size > 0 && parameterClass.fields?size > 0)>,
    </#if><#if (parameterClass.valueTypes?size = 1)>val value: String<#elseif (parameterClass.valueTypes?size > 1)>values: Array<String></#if>
)