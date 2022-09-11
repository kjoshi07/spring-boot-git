pipeline {
    agent any

    triggers {
        pollSCM 'H * * * *'
    }
    stages {
        stage('Build') {
            steps {
                 script {
                    echo 'Pulling...' + env.BRANCH_NAME
                    def mvnHome = tool 'Maven 3.5.2'
                    if (isUnix()) {
                                            def targetVersion = getDevVersion()
                                            print 'target build version...'
                                            print targetVersion
                                            sh "'${mvnHome}/bin/mvn' -Dintegration-tests.skip=true -Dbuild.number=${targetVersion} clean package"
                                            def pom = readMavenPom file: 'pom.xml'
                                            // get the current development version
                                            developmentArtifactVersion = "${pom.version}-${targetVersion}"
                                            print pom.version
                                            // execute the unit testing and collect the reports
                                            junit '**//*target/surefire-reports/TEST-*.xml'
                                            archive 'target*//*.jar'
                                        } else {
                                            bat(/"${mvnHome}\bin\mvn" -Dintegration-tests.skip=true clean package/)
                                            def pom = readMavenPom file: 'pom.xml'
                                            print pom.version
                                            junit '**//*target/surefire-reports/TEST-*.xml'
                                            archive 'target*//*.jar'
                                        }
            }
        }
        stage('Test') {
            steps {
                 script {
                                    def mvnHome = tool 'Maven 3.5.2'
                                    if (isUnix()) {
                                        // just to trigger the integration test without unit testing
                                        sh "'${mvnHome}/bin/mvn'  verify -Dunit-tests.skip=true"
                                    } else {
                                        bat(/"${mvnHome}\bin\mvn" verify -Dunit-tests.skip=true/)
                                    }

                                }
            }
        }
    }
}