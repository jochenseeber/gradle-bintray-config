/**
 * BSD 2-Clause License
 *
 * Copyright (c) 2016, Jochen Seeber
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

import java.io.IOException;

import org.eclipse.jdt.annotation.Nullable;
import org.gradle.api.internal.ConventionTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;

/**
 * Create a Github repository for the project
 */
public class ConfigureBintrayPackage extends ConventionTask {

    /**
     * Name of the repository
     */
    @Input
    private @Nullable String repositoryName;

    /**
     * Description of the repository
     */
    @Input
    private @Nullable String repositoryDescription;

    /**
     * Name of the repository
     */
    @Input
    private @Nullable String packageName;

    /**
     * Description of the repository
     */
    @Input
    private @Nullable String packageDescription;

    /**
     * Bintray user
     */
    private @Nullable String bintrayUser;

    /**
     * Bintray user
     */
    private @Nullable String bintrayPassword;

    /**
     * Create the repository on Github
     *
     * @throws IOException if something goes horribly wrong
     */
    @TaskAction
    public void configurePackage() throws IOException {

    }

    /**
     * Get the name of the repository
     *
     * @return Name of the repository
     */
    public @Nullable String getRepositoryName() {
        return this.repositoryName;
    }

    /**
     * Set the name of the repository
     *
     * @param name Name of the repository
     */
    public void setRepositoryName(@Nullable String name) {
        this.repositoryName = name;
    }

    /**
     * Get the description of the repository
     *
     * @return Description of the repository
     */
    public @Nullable String getRepositoryDescription() {
        return this.repositoryDescription;
    }

    /**
     * Set the description of the repository
     *
     * @param description Description of the repository
     */
    public void setRepositoryDescription(@Nullable String description) {
        this.repositoryDescription = description;
    }

    /**
     * Get the package name
     *
     * @return Package name
     */
    public @Nullable String getPackageName() {
        return this.packageName;
    }

    /**
     * Set the package name
     *
     * @param packageName Package name
     */
    public void setPackageName(@Nullable String packageName) {
        this.packageName = packageName;
    }

    /**
     * Get the package description
     *
     * @return Package description
     */
    public @Nullable String getPackageDescription() {
        return this.packageDescription;
    }

    /**
     * Set the package description
     *
     * @param packageDescription Package description
     */
    public void setPackageDescription(@Nullable String packageDescription) {
        this.packageDescription = packageDescription;
    }

    /**
     * Get the Bintray user
     *
     * @return Bintray user
     */
    @Internal
    public @Nullable String getBintrayUser() {
        return this.bintrayUser;
    }

    /**
     * Set the Bintray user
     *
     * @param bintrayUser Bintray user
     */
    public void setBintrayUser(@Nullable String bintrayUser) {
        this.bintrayUser = bintrayUser;
    }

    /**
     * Get the Bintray password
     *
     * @return Bintray password
     */
    @Internal
    public @Nullable String getBintrayPassword() {
        return this.bintrayPassword;
    }

    /**
     * Set the Bintray password
     *
     * @param bintrayPassword Bintray password
     */
    public void setBintrayPassword(@Nullable String bintrayPassword) {
        this.bintrayPassword = bintrayPassword;
    }

}
