<#ftl output_format="HTML" auto_esc=true>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <style>
        body { margin: 16px; color: #172033; font: 14px system-ui, sans-serif; }
        h2 { font-size: 15px; margin: 16px 0 6px; }
        pre { white-space: pre-wrap; overflow-wrap: anywhere; margin: 0; }
        .summary, pre { background: #f3f6fc; border: 1px solid #dbe3f0; border-radius: 6px; padding: 10px; }
        strong { color: #244f9e; }
        dt { font-weight: 600; }
        dd { margin: 0 0 6px; }
    </style>
</head>
<body>
<div class="summary"><strong>${data.method}</strong> ${data.url}</div>
<#if (data.headers)?has_content>
    <h2>Headers</h2>
    <dl>
        <#list data.headers as name, value>
            <#if ["accept", "content-type", "origin", "x-requested-with"]?seq_contains(name?lower_case)>
                <dt>${name}</dt><dd>${value}</dd>
            </#if>
        </#list>
    </dl>
</#if>
<#if (data.body)?has_content>
    <h2>Body</h2>
    <pre>${data.body}</pre>
</#if>
<#if (data.formParams)?has_content>
    <h2>Form data</h2>
    <dl>
        <#list data.formParams as name, value>
            <#if name == "lang" || name?starts_with("variant_ids[")>
                <dt>${name}</dt><dd>${value}</dd>
            </#if>
        </#list>
    </dl>
</#if>
</body>
</html>
