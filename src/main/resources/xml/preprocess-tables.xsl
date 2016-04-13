<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
		xmlns:dc="http://purl.org/dc/elements/1.1/"
		xmlns:html="http://www.w3.org/1999/xhtml"
		xmlns:dtb="http://www.daisy.org/z3986/2005/dtbook/"
		xmlns="http://www.daisy.org/z3986/2005/dtbook/"
		xpath-default-namespace="http://www.daisy.org/z3986/2005/dtbook/"
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

	<xsl:template match="//table[contains(., '/')]">
	  <xsl:call-template name="PROCESSED_TABLE"/>
	</xsl:template>

	<xsl:template name="PROCESSED_TABLE">
	  <xsl:copy>
	    <xsl:attribute name="class">dash-separated</xsl:attribute>
	    <xsl:apply-templates select="@*|node()"/>
	  </xsl:copy>
	</xsl:template>

</xsl:stylesheet>
