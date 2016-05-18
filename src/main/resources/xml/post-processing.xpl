<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step type="celia:post-processing" version="1.0"
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
    
    <p:option name="make-volumes-divisible-by-four" select="'false'"/>
    
    <p:output port="result"/>
    
    <p:import href="http://www.daisy.org/pipeline/modules/common-utils/library.xpl"/>
        
    <p:parameters name="parameters"/>
    
    <p:identity>
        <p:input port="source">
            <p:pipe port="source" step="main"/>
        </p:input>
    </p:identity>
    
    <p:choose>
      <p:when test="$make-volumes-divisible-by-four='true'">
        <px:message message="Running PEF post-processing"/>
        <p:xslt>
          <p:input port="parameters">
            <p:pipe port="result" step="parameters"/>
          </p:input>
          <p:input port="stylesheet">
            <p:document href="postprocess-volumes.xsl"/>
          </p:input>
        </p:xslt>
      </p:when>
      <p:otherwise>
        <px:message message="Skipping PEF post-processing"/>
      </p:otherwise>
    </p:choose>

    <px:message message="Finished running Celia-specific post-processing steps" severity="DEBUG"/>
    
</p:declare-step>
