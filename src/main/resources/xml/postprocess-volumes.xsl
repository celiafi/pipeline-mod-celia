<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:pef="http://www.daisy.org/ns/2008/pef"
		xmlns="http://www.daisy.org/ns/2008/pef"
		xpath-default-namespace="http://www.daisy.org/ns/2008/pef"
		exclude-result-prefixes="#all"
		version="2.0">
	
	<xsl:output indent="yes"/>

	<xsl:param name="duplex" select="'true'"/>

	<!-- Generic copy-all template -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

        <!-- Insert empty pages if number of pages mod 4 != 0 -->
        <xsl:template match="volume/section[last()]/page[last()]">
	  <xsl:copy-of select="."/>
	  <xsl:choose>
	    <xsl:when test="$duplex = 'true'">
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
	    </xsl:when>
	    <xsl:otherwise>
	      <xsl:if test="(count(../../section/page) mod 2) = 1">
	        <page/>
	      </xsl:if>
	    </xsl:otherwise>
	  </xsl:choose>
	</xsl:template>

</xsl:stylesheet>
