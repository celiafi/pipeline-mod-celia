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

        <xsl:template match="volume/section[last()]/page[last()]">
	  <xsl:copy-of select="."/>
	  <xsl:choose>
	    <xsl:when test="$duplex = 'true'">
	      <xsl:choose>
                <!-- Insert a single empty page if the last page is already empty,
	             otherwise insert an empty sheet, ie. 2 empty pages -->
	        <xsl:when test="not(*) and not(normalize-space())">
	          <page/>
	        </xsl:when>
	        <xsl:otherwise>
	          <page/>
	          <page/>
	        </xsl:otherwise>
	      </xsl:choose>
	    </xsl:when>
	    <xsl:otherwise>
	      <!-- When printing single-sided, just add a single empty page
	           if the last page is not empty. -->
	      <xsl:if test="* or normalize-space()">
	        <page/>
	      </xsl:if>
	    </xsl:otherwise>
	  </xsl:choose>
	</xsl:template>


</xsl:stylesheet>
