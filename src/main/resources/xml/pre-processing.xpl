<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step type="celia:pre-processing" version="1.0"
                 xmlns:celia="http://www.celia.fi"
                 xmlns:p="http://www.w3.org/ns/xproc"
                 xmlns:px="http://www.daisy.org/ns/pipeline/xproc"
                 xmlns:d="http://www.daisy.org/ns/pipeline/data"
                 xmlns:c="http://www.w3.org/ns/xproc-step"
                 xmlns:pef="http://www.daisy.org/ns/2008/pef"
                 exclude-inline-prefixes="#all"
                 name="main">
    
    <p:input port="source"/>
    <p:input port="parameters" kind="parameter"/>
    <p:output port="result"/>
    
    <p:import href="http://www.daisy.org/pipeline/modules/common-utils/library.xpl"/>
        
    <p:parameters name="parameters"/>
    <p:identity>
        <p:input port="source">
            <p:pipe port="source" step="main"/>
        </p:input>
    </p:identity>
    
    <px:message message="Running Celia-specific pre-processing steps"/>
    <p:xslt>
        <p:input port="parameters">
            <p:pipe port="result" step="parameters"/>
        </p:input>
        <p:input port="stylesheet">
            <p:document href="insert-titlepage.xsl"/>
        </p:input>
    </p:xslt>
    <px:message message="Finished running Celia-specific pre-processing steps" severity="DEBUG"/>
    
</p:declare-step>