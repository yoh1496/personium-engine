/**
 * Personium
 * Copyright 2014-2021 - Personium Project Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.personium.engine.rs;

import javax.ws.rs.Path;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.personium.engine.PersoniumEngineException;
import io.personium.engine.source.FsServiceResourceSourceManager;
import io.personium.engine.source.ISourceManager;

/**
 * Only JAX-RS Resource class used in Personium-Engine for production use.
 */
@Path("/{cell}/{schema}/service/{id : .+}")
public class ServiceResource extends AbstractService {
    /** Logger Object. */
    private static Log log = LogFactory.getLog(AbstractService.class);

    static {
        log.getClass();
    }

    /**
     * Constructor.
     * @throws PersoniumEngineException PersoniumEngine例外
     */
    public ServiceResource() throws PersoniumEngineException {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getCell() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getSchemaURI() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ISourceManager getServiceCollectionManager() throws PersoniumEngineException {
        // return FsServiceResourceSourceManager instance (production use)
        return new FsServiceResourceSourceManager(this.getFsPath(), this.getFsRoutingId());
    }
}
