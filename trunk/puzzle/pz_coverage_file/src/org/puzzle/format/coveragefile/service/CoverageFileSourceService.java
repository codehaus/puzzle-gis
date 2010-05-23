/*
 *    Puzzle GIS - Desktop GIS Platform
 *    http://puzzle-gis.codehaus.org
 *
 *    (C) 2007-2009, Johann Sorel
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 3 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.puzzle.format.coveragefile.service;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageReaderWriterSpi;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.geotoolkit.coverage.WorldImageReaderUtilities;
import org.geotoolkit.image.io.plugin.AsciiGridReader;
import org.geotoolkit.image.io.plugin.DimapImageReader;
import org.geotoolkit.image.io.plugin.NetcdfImageReader;
import org.geotoolkit.image.io.plugin.WorldFileImageReader;

import org.openide.util.Exceptions;

import org.puzzle.core.project.source.AbstractGISSourceService;
import org.puzzle.core.project.source.capabilities.FileSourceCreation;
import org.puzzle.core.project.source.GISSource;
import org.puzzle.core.project.source.GISSourceInfo;
import org.puzzle.format.coveragefile.resources.CFResource;

/**
 * This is the service linked with the {@code CoverageFileSource}.
 * The service is used to manage this kind of source.
 * 
 * @author  Johann Sorel (Puzzle-GIS)
 * @see     org.puzzle.core.project.source.GISFileSourceService
 */
public class CoverageFileSourceService extends AbstractGISSourceService implements FileSourceCreation{
    private static final String SERVICE_ID = "CoverageFile";
    
    /** {@inheritDoc} */
    @Override
    public String getIdentifier() {
        return SERVICE_ID;
    }

    /** {@inheritDoc} */
    @Override
    public String getTitle() {
        return CFResource.getString("worldTitle");
    }

    /** {@inheritDoc} */
    @Override
    public GISSource restoreSource(final GISSourceInfo info) throws IllegalArgumentException {
        final String strURI = info.getParameters().get("uri").toString();
        
        if(strURI == null) throw new IllegalArgumentException("missing parameter uri");

        File worldImage = null;
        try{
            worldImage = new File(new URI(strURI));
        }catch(URISyntaxException urise){
            Exceptions.printStackTrace(urise);
        }

        return new CoverageFileSource(info,worldImage);
    }

    /** {@inheritDoc} */
    @Override
    public GISSourceInfo createSourceInfo(final File file) throws IllegalArgumentException {
        final String uri = file.toURI().toString();
        final Map<String,Serializable> params = new HashMap<String, Serializable>();
        params.put("uri", uri);
        return new GISSourceInfo(GISSourceInfo.UNREGISTERED_ID, SERVICE_ID, params);
    }
    
    /** {@inheritDoc} */
    @Override
    public Collection<FileFilter> createFilters() {
        final List<FileFilter> filters = new ArrayList<FileFilter>();

        final Locale locale = Locale.getDefault();
        final IIORegistry registry = IIORegistry.getDefaultInstance();
        final Iterator<? extends ImageReaderWriterSpi> it = registry.getServiceProviders(ImageReaderSpi.class, true);
        final Map<String,String> suffixDone = new HashMap<String,String>();
        final Set<String> formatsDone = new HashSet<String>();
        final StringBuilder buffer = new StringBuilder();

        skip:
        while (it.hasNext()) {
            final ImageReaderWriterSpi spi = it.next();

            //todo hard coded list of knownec coverage readers
            //must find a different way to filter spi whithout knowing them
//            if(!(spi instanceof NetcdfImageReader.Spi
//               || spi instanceof DimapImageReader.Spi
//               || spi instanceof WorldFileImageReader.Spi
//               || spi instanceof AsciiGridReader.Spi)){
//                continue skip;
//            }


            String longFormat = null;
            for (final String format : spi.getFormatNames()) {
                if (!formatsDone.add(format)) {
                    // Avoid declaring the same format twice (e.g. declaring
                    // both the JSE and JAI ImageReaders for the PNG format).
                    continue skip;
                }
                // Remember the longuest format string. If two of them
                // have the same length, favor the one in upper case.
                longFormat = longuest(longFormat, format);
            }
            if (longFormat == null) {
                longFormat = spi.getDescription(locale);
            }
            /*
             * At this point, we have a provider to take in account. We need to get the list of
             * suffixes, but we don't need both the lower-case and upper-case flavors of the same
             * suffix. If those two flavors exist, then we will keep only the first one (which is
             * usually the lower-case flavor). The iteration is performed in reverse order for that
             * reason.
             */
            String[] suffix = spi.getFileSuffixes();
            for (int i=suffix.length; --i >= 0;) {
                final String s = suffix[i].trim();
                if (s.length() != 0) {
                    suffixDone.put(s.toLowerCase(locale), s);
                }
            }
            if (!suffixDone.isEmpty()) {
                suffix = suffixDone.values().toArray(new String[suffixDone.size()]);
                suffixDone.clear();
                buffer.setLength(0);
                buffer.append(longFormat);
                String separator = "  (";
                for (final String s : suffix) {
                    buffer.append(separator).append("*.").append(s);
                    separator = ", ";
                }
                buffer.append(')');
                final FileFilter filter = new FileNameExtensionFilter(buffer.toString(), suffix);
                filters.add(filter);
            }
        }

        return filters;
    }

    /**
     * Selects the longuest format string. If two of them
     * have the same length, favor the one in upper case.
     *
     * @param current    The previous longuest format string, or {@code null} if none.
     * @param candidate  The format string which may be longuer than the previous one.
     * @return The format string which is the longuest one up to date.
     */
    static String longuest(final String current, final String candidate) {
        if (current != null) {
            final int dl = candidate.length() - current.length();
            if (dl < 0 || (dl == 0 && candidate.compareTo(current) >= 0)) {
                return current;
            }
        }
        return candidate;
    }

}
