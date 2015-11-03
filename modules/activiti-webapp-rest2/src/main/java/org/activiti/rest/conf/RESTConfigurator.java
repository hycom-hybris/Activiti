/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.activiti.rest.conf;

import org.activiti.engine.cfg.AbstractProcessEngineConfigurator;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

import org.activiti.rest.conf.factory.HybrisGroupManagerFactory;
import org.activiti.rest.conf.factory.HybrisUserManagerFactory;


public class RESTConfigurator extends AbstractProcessEngineConfigurator {


    @Override
    public void configure(ProcessEngineConfigurationImpl processEngineConfiguration) {

        HybrisUserManagerFactory hybrisUserManagerFactory = new HybrisUserManagerFactory();
        processEngineConfiguration.getSessionFactories().put(hybrisUserManagerFactory.getSessionType(), hybrisUserManagerFactory);

        HybrisGroupManagerFactory hybrisGroupManagerFactory = new HybrisGroupManagerFactory();
        processEngineConfiguration.getSessionFactories().put(hybrisGroupManagerFactory.getSessionType(), hybrisGroupManagerFactory);

    }

    @Override
    public void beforeInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        // DO NOTHING
    }

}
