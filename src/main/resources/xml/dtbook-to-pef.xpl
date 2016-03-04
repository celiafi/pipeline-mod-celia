<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step type="celia:dtbook-to-pef" version="1.0"
                xmlns:celia="http://www.celia.fi"
		xmlns:p="http://www.w3.org/ns/xproc"
		xmlns:px="http://www.daisy.org/ns/pipeline/xproc"
		exclude-inline-prefixes="#all"
		name="main">

    <p:documentation xmlns="http://www.w3.org/1999/xhtml">
        <h1 px:role="name">DTBook to PEF (Celia)</h1>
	<p px:role="desc">Transforms a DTBook (DAISY 3 XML) document into a PEF.</p>
    </p:documentation>

    <p:input port="source" primary="true" px:name="source" px:media-type="application/x-dtbook+xml"/>

    <p:option name="pef-output-dir"/>
    <p:option name="temp-dir"/>

    <p:option name="stylesheet" select="'http://www.celia.fi/pipeline/modules/braille/default.css'"/>

    <p:option name="transform" px:data-type="transform-query"/>

    <p:import href="http://www.daisy.org/pipeline/modules/braille/dtbook-to-pef/dtbook-to-pef.xpl"/>

    <px:dtbook-to-pef>
        <p:with-option name="transform" select="'(translator:celia)'"/>
	<p:with-option name="pef-output-dir" select="$pef-output-dir"/>
	<p:with-option name="stylesheet" select="$stylesheet"/>
    </px:dtbook-to-pef>

</p:declare-step>
