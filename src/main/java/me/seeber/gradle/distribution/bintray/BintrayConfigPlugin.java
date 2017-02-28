/**
 * BSD 2-Clause License
 *
 * Copyright (c) 2016-2017, Jochen Seeber
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package me.seeber.gradle.distribution.bintray;

import org.gradle.api.Task;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.model.Defaults;
import org.gradle.model.Model;
import org.gradle.model.ModelMap;
import org.gradle.model.Mutate;
import org.gradle.model.RuleSource;
import org.gradle.model.internal.core.Hidden;

import com.jfrog.bintray.gradle.BintrayExtension;
import com.jfrog.bintray.gradle.BintrayExtension.PackageConfig;
import com.jfrog.bintray.gradle.BintrayExtension.VersionConfig;
import com.jfrog.bintray.gradle.BintrayPlugin;

import me.seeber.gradle.distribution.bintray.BintrayConfigPlugin.PluginRules.GithubPluginRules;
import me.seeber.gradle.distribution.maven.MavenConfigPlugin;
import me.seeber.gradle.plugin.AbstractProjectConfigPlugin;
import me.seeber.gradle.project.base.ProjectConfig;
import me.seeber.gradle.project.base.ProjectConfigPlugin;
import me.seeber.gradle.project.base.ProjectContext;
import me.seeber.gradle.repository.github.GithubConfig;

/**
 * Configure a project to use Bintray as a repository
 */
public class BintrayConfigPlugin extends AbstractProjectConfigPlugin {

    /**
     * Plugin rules
     */
    public static class PluginRules extends RuleSource {

        /**
         * Provide the Bintray configuration
         *
         * @param bintrayConfig Bintray configuration
         */
        @Model
        public void bintrayConfig(BintrayConfig bintrayConfig) {
        }

        /**
         * Initialize the Bintray configuration
         *
         * @param bintrayConfig Bintray configuration to initialize
         */
        @Defaults
        public void initializeBintrayConfig(BintrayConfig bintrayConfig) {
            bintrayConfig.setRepository("maven");
        }

        /**
         * Provide the Bintray plugin's configuration
         *
         * @param extensions Extension container to look up configuration
         * @return Bintray plugin's configuration
         */
        @Model
        @Hidden
        public BintrayExtension bintrayExtension(ExtensionContainer extensions) {
            return extensions.getByType(BintrayExtension.class);
        }

        /**
         * Make tasks depend on Bintray plugin's configuration
         *
         * @param tasks Task container
         * @param bintrayExtension Bintray plugin's configuration
         */
        @Mutate
        public void tasksDependOnBintrayExtension(ModelMap<Task> tasks, BintrayExtension bintrayExtension) {
        }

        /**
         * Configure the Bintray plugin
         *
         * <ul>
         * <li>Set Bintray user and password from Gradle properties file or environment
         * <li>Configure the Bintray package
         * </ul>
         *
         * @param bintray Bintray plugin's configuration
         * @param projectConfig Project configuration
         * @param bintrayConfig Bintray configuration
         * @param project Project context
         */
        @Defaults
        public void configureBintrayExtension(BintrayExtension bintray, ProjectConfig projectConfig,
                BintrayConfig bintrayConfig, ProjectContext project) {
            bintray.setUser(project.getProperty("bintray.user"));
            bintray.setKey(project.getProperty("bintray.key"));

            PackageConfig pkg = bintray.getPkg();

            pkg.setName(project.getName());
            pkg.setDesc(project.getDescription());
            pkg.setRepo(bintrayConfig.getRepository());
            pkg.setVcsUrl(projectConfig.getRepository().getConnection());
            pkg.setWebsiteUrl(projectConfig.getWebsiteUrl());
            pkg.setIssueTrackerUrl(projectConfig.getIssueTracker().getWebsiteUrl());
            pkg.setLicenses(projectConfig.getLicense().getId());

            VersionConfig version = pkg.getVersion();
            version.setName(project.getVersion().toString());
            version.setVcsTag(project.getName() + "-" + project.getVersion());
        }

        /**
         * Configure the Bintray publication
         *
         * <ul>
         * <li>Add all publications to the Bintray publications
         * </ul>
         *
         * @param bintrayExtension Bintray extension to configure
         * @param publishingExtension Publishing extension to look up publications
         */
        @Mutate
        public void configureBintrayPublication(BintrayExtension bintrayExtension,
                PublishingExtension publishingExtension) {
            String[] publications = bintrayExtension.getPublications();

            if (publications == null || publications.length == 0) {
                publications = publishingExtension.getPublications().stream().map(p -> p.getName())
                        .toArray(s -> new String[s]);
                bintrayExtension.setPublications(publications);
            }
        }

        /**
         * Plugin rules
         */
        public static class GithubPluginRules extends RuleSource {

            /**
             * Add GitHub configuration
             *
             * @param bintrayConfig Bintray configuration
             * @param githubConfig Github configuration
             */
            @Defaults
            public void configureBintrayExtension(BintrayExtension bintrayConfig, GithubConfig githubConfig) {
                PackageConfig pkg = bintrayConfig.getPkg();
                pkg.setGithubRepo(String.format("%s/%s", githubConfig.getUser(), githubConfig.getRepository()));
            }

        }
    }

    /**
     * Initialize the Bintray configuration plugin
     *
     * <ul>
     * <li>Apply Project Config Plugin
     * <li>Apply Maven Config Plugin
     * <li>Apply Bintray Plugin
     * </ul>
     *
     * @see me.seeber.gradle.plugin.AbstractProjectConfigPlugin#initialize()
     */
    @Override
    public void initialize() {
        getProject().getPluginManager().apply(ProjectConfigPlugin.class);
        getProject().getPluginManager().apply(MavenConfigPlugin.class);
        getProject().getPluginManager().apply(BintrayPlugin.class);

        getProject().afterEvaluate(p -> {
            // The Bintray plugin configures its tasks in a {@link BuildListener}, so we need to force realization of
            // the extension in order to make it available before the plugin accesses it. They could've just used a
            // ConventionPlugin...
            ((ProjectInternal) p).getModelRegistry().realize("bintrayExtension", BintrayExtension.class);
        });

        getProject().getPluginManager().withPlugin("me.seeber.github", p -> {
            getProject().getPluginManager().apply(GithubPluginRules.class);
        });
    }

}
