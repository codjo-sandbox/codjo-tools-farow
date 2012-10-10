Framework codjo.net
===================

This library is part of the [framework codjo.net](http://codjo.net) effort.


To build Farow, you need to declare the following properties (for example in your settings.xml).

for example :
```
        <profile>
            <id>profil-tools</id>
            <activation>
                <property>
                    <name>!alwaysActivated</name>
                </property>
            </activation>
            <properties>
                <githubAccount>GITHUB_ACCOUNT_NAME</githubAccount>
                <githubPassword>GITHUB_ACCOUNT_PASSWORD</githubPassword>

                <nexusAccount>NEXUS_ACCOUNT_NAME</nexusAccount>
                <nexusPassword>NEXUS_ACCOUNT_PASSWORD</nexusPassword>
                <nexusHostUrl>NEXUS_ACCOUNT_PASSWORD</nexusHostUrl>

            </properties>
        </profile>
```

TODO :
* Replace hard coded paths.
* Manage Ontologie

* Manage stabilisation type "patch" or release candidate
* Manage plugin codjo artifacts version (as it is needed for stabilisation)
* Refactoring NexusApi/Teamlab --> http communication layer ?
* Add a configuration GUI
* Refactoring of main form
* Enhance teamlab connection (awfull GUI)
* Github interaction :
   - verify pull requests before starting stabilisation
   - forked project deletion
   - refork super-pom at the end of the process
* Bug : si on clique sur "Annuler" on a un warning le numéro de version ne doit pas être null...