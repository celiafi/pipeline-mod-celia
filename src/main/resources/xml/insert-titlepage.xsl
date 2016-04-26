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
	<xsl:variable name="PID" as="xs:string*"
		select="/dtbook/head/meta[@name eq 'dtb:uid']/@content|
		/html:html/html:head/dc:identifier/text()"/>
	<xsl:variable name="SOURCE_ISBN" as="xs:string*"
		select="(/dtbook/head/meta[@name eq 'dc:source']/@content|
		/html:html/html:head/dc:source/text())/replace(
		., '^urn:isbn:', '')"/>
	<xsl:variable name="TITLE" as="xs:string*"
		select="//meta[@name='dc:Title']/@content"/>
	<xsl:variable name="AUTHOR" as="xs:string*"
		select="//meta[@name='dc:Creator']/@content"/>
	<xsl:variable name="YEAR" as="xs:integer"
		select="year-from-date(current-date())"/>
	<xsl:param name="show-braille-page-numbers" select="'false'"/>
	
	<!-- Generic copy-all template -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

    <!-- DTBook template -->
    <xsl:template match="frontmatter/doctitle">
        <xsl:next-match/>
	<level1 id="generated-title-page">
	    <xsl:call-template name="TITLE_PAGE_CONTENT"/>
	</level1>
    </xsl:template>

    <xsl:template name="TITLE_PAGE_CONTENT">
    	<xsl:element name="p" namespace="{$OUTPUT_NAMESPACE}">
    		<xsl:attribute name="style" select="'display:block'"/>
    		<xsl:value-of select="$TITLE"/>
    	</xsl:element>
        <xsl:element name="p" namespace="{$OUTPUT_NAMESPACE}">
		<xsl:attribute name="style" select="'display:block; margin-top:1;'"/>
	    	<xsl:value-of select="$AUTHOR"/>
	</xsl:element>
    	<xsl:element name="p" namespace="{$OUTPUT_NAMESPACE}">
    		<xsl:attribute name="style" select="'display:block; margin-top:1;'"/>
    		<xsl:text>Vihko </xsl:text>
    		<xsl:element name="span" namespace="{$OUTPUT_NAMESPACE}">
    			<xsl:attribute name="style" select="'::before { content: -obfl-evaluate(&quot;(round $volume)&quot;); }'"/>
    		</xsl:element>
		<xsl:text>/</xsl:text>
    		<xsl:element name="span" namespace="{$OUTPUT_NAMESPACE}">
    			<xsl:attribute name="style" select="'::before { content: -obfl-evaluate(&quot;(round $volumes)&quot;); }'"/>
    		</xsl:element>
    	</xsl:element>
    </xsl:template>

</xsl:stylesheet>
