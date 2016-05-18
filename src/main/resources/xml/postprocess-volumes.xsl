<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
		xmlns:dc="http://purl.org/dc/elements/1.1/"
		xmlns:html="http://www.w3.org/1999/xhtml"
		xmlns:dtb="http://www.daisy.org/z3986/2005/dtbook/"
		xmlns:pef="http://www.daisy.org/ns/2008/pef"
		xmlns="http://www.daisy.org/ns/2008/pef"
		xpath-default-namespace="http://www.daisy.org/ns/2008/pef"
		exclude-result-prefixes="#all"
		version="2.0">
	
	<xsl:output indent="yes"/>

    <!-- Variables -->
    <xsl:variable name="OUTPUT_NAMESPACE" as="xs:string" select="namespace-uri(/*)"/>
	
	<!-- Generic copy-all template -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

        <!-- Insert empty pages if number of pages mod 4 != 0 -->
        <xsl:template match="volume/section/page[last()]">
	  <xsl:copy-of select="."/>
	  <xsl:if test="(count(../../section/page) mod 4) = 3">
	    <page/>
	  </xsl:if>
	  <xsl:if test="(count(../../section/page) mod 4) = 2">
	    <page/>
	    <page/>
	  </xsl:if>
	  <xsl:if test="(count(../../section/page) mod 4) = 1">
	    <page/>
	    <page/>
	    <page/>
	  </xsl:if>
	</xsl:template>


</xsl:stylesheet>
