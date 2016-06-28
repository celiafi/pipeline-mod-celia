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
    
    <p:option name="pad-volume-endings" select="'false'"/>
    <p:option name="make-volumes-divisible-by-four" select="'false'"/>
    <p:option name="duplex" select="'true'"/>
    
    <p:output port="result"/>
    
    <p:import href="http://www.daisy.org/pipeline/modules/common-utils/library.xpl"/>
        
    <p:parameters name="parameters"/>
    
    <p:identity>
        <p:input port="source">
            <p:pipe port="source" step="main"/>
        </p:input>
    </p:identity>

    <p:choose>
      <p:when test="$pad-volume-endings='true'">
        <px:message message="Inserting empty pages at the end of the volumes to protect the print"/>
        <p:xslt>
	  <p:with-param name="duplex" select="$duplex"/>
          <p:input port="parameters">
            <p:pipe port="result" step="parameters"/>
          </p:input>
          <p:input port="stylesheet">
            <p:document href="insert-empty-pages.xsl"/>
          </p:input>
        </p:xslt>
      </p:when>
      <p:otherwise>
        <px:message message="Skipping insertion of protection pages"/>
      </p:otherwise>
    </p:choose>
    
    <p:choose>
      <p:when test="$make-volumes-divisible-by-four='true'">
        <px:message message="Inserting pages to make the volumes divisible by four"/>
        <!-- Add explicit empty page at the end of each section, if necessary -->
        <!-- This makes counting the pages easier in the next stage -->
        <p:xslt>
          <p:with-param name="duplex" select="$duplex"/>
          <p:input port="parameters">
            <p:pipe port="result" step="parameters"/>
          </p:input>
          <p:input port="stylesheet">
            <p:document href="pad-sections.xsl"/>
          </p:input>
        </p:xslt>
        <p:xslt>
	  <p:with-param name="duplex" select="$duplex"/>
          <p:input port="parameters">
            <p:pipe port="result" step="parameters"/>
          </p:input>
          <p:input port="stylesheet">
            <p:document href="postprocess-volumes.xsl"/>
          </p:input>
        </p:xslt>
      </p:when>
      <p:otherwise>
        <px:message message="Skipping volume length correction"/>
      </p:otherwise>
    </p:choose>

    <px:message message="Finished running Celia-specific post-processing steps" severity="DEBUG"/>
    
</p:declare-step>
