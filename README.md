# Nuxeo: File System Blob Importer

Inspired by existing file system binaries [blog post](https://www.nuxeo.com/blog/referencing-existing-file-system-binaries-in-a-nuxeo-repository/)

## Dependencies

Requires `nuxeo-platform-importer` package.

## Build and Install

Build with maven (at least 3.3)

```
mvn clean install
```
> JAR built here: `target`

> Copy to `nxserver/lib` directory of your Nuxeo installation.

## Configure (nuxeo.conf)

FileSystem Provider (optional):

```
# Name of the filesystem blob provider
filesystem.blob.provider=fs
```

### Studio Configuration

```xml
<extension target="org.nuxeo.ecm.core.blob.BlobManager" point="configuration">
  <blobprovider name="fs">
    <class>org.nuxeo.ecm.core.blob.FilesystemBlobProvider</class>
    <property name="root">/tmp/nuxeo/import</property>
    <property name="preventUserUpdate">true</property>
  </blobprovider>
</extension>

<extension target="org.nuxeo.ecm.platform.importer.service.DefaultImporterComponent" point="importerConfiguration">
  <importerConfig sourceNodeClass ="org.nuxeo.ecm.platform.importer.source.FileSourceNode" >
    <documentModelFactory documentModelFactoryClass="org.nuxeo.ecm.platform.importer.externalblob.factories.FileSystemDocumentModelFactory" />
  </importerConfig>
</extension>
```

## Support

**These features are sand-boxed and not yet part of the Nuxeo Production platform.**

These solutions are provided for inspiration and we encourage customers to use them as code samples and learning resources.

This is a moving project (no API maintenance, no deprecation process, etc.) If any of these solutions are found to be useful for the Nuxeo Platform in general, they will be integrated directly into platform, not maintained here.

## Licensing

[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)

## About Nuxeo

Nuxeo dramatically improves how content-based applications are built, managed and deployed, making customers more agile, innovative and successful. Nuxeo provides a next generation, enterprise ready platform for building traditional and cutting-edge content oriented applications. Combining a powerful application development environment with SaaS-based tools and a modular architecture, the Nuxeo Platform and Products provide clear business value to some of the most recognizable brands including Verizon, Electronic Arts, Sharp, FICO, the U.S. Navy, and Boeing. Nuxeo is headquartered in New York and Paris.

More information is available at [www.nuxeo.com](http://www.nuxeo.com).

