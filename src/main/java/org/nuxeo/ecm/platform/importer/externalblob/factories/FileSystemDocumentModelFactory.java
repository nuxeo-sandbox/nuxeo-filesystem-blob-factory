package org.nuxeo.ecm.platform.importer.externalblob.factories;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.blob.BlobInfo;
import org.nuxeo.ecm.core.blob.BlobManager;
import org.nuxeo.ecm.core.blob.FilesystemBlobProvider;
import org.nuxeo.ecm.platform.filemanager.api.FileManager;
import org.nuxeo.ecm.platform.importer.factories.DefaultDocumentModelFactory;
import org.nuxeo.ecm.platform.importer.source.SourceNode;
import org.nuxeo.runtime.api.Framework;

public class FileSystemDocumentModelFactory extends DefaultDocumentModelFactory {

    protected FileManager fileManager;

    protected FilesystemBlobProvider blobProvider;

    /**
     * Instantiate a FileSystemDocumentModelFactory that creates Folder and File
     */
    public FileSystemDocumentModelFactory() {
        this("Folder", "File");
    }

    /**
     * Instantiate a FileSystemDocumentModelFactory that creates specified types doc
     *
     * @param folderishType the folderish type
     * @param leafType the other type
     */
    public FileSystemDocumentModelFactory(String folderishType, String leafType) {
        this.folderishType = folderishType;
        this.leafType = leafType;
    }

    @Override
    public DocumentModel createLeafNode(CoreSession session, DocumentModel parent, SourceNode node) throws IOException {
        Blob blob = null;
        Map<String, Serializable> props = null;
        String leafTypeToUse = leafType;
        BlobHolder bh = node.getBlobHolder();
        if (bh != null) {
            File file = bh.getBlob().getFile();
            BlobInfo blobInfo = new BlobInfo();
            blobInfo.key = file.getAbsolutePath();
            
            blob = getBlobProvider().createBlob(blobInfo);
            
            props = bh.getProperties();
            String bhType = getDocTypeToUse(bh);
            if (bhType != null) {
                leafTypeToUse = bhType;
            }
        }
        
        String fileName = node.getName();
        String name = getValidNameFromFileName(fileName);
        String path = parent.getPathAsString();
        DocumentModel doc = session.createDocumentModel(path, name, leafTypeToUse);
        for (String facet : getFacetsToUse(bh)) {
            doc.addFacet(facet);
        }
        doc.setProperty("dublincore", "title", node.getName());
        if (blob != null) {
            blob.setFilename(fileName);
            doc.setProperty("file", "content", blob);
        }
        doc = session.createDocument(doc);
        if (props != null) {
            doc = setDocumentProperties(session, props, doc);
        }
        return doc;
    }

    protected FileManager getFileManager() {
        if (fileManager == null) {
            fileManager = Framework.getService(FileManager.class);
        }
        return fileManager;
    }

    protected FilesystemBlobProvider getBlobProvider() {
        if (blobProvider == null) {
            String providerId = Framework.getProperty("filesystem.blob.provider", "fs");
            BlobManager bm = Framework.getService(BlobManager.class);
            blobProvider = (FilesystemBlobProvider) bm.getBlobProvider(providerId);
            if (blobProvider == null) {
                throw new NullPointerException("No such blob provider: " + providerId);
            }
        }
        return blobProvider;
    }

}
