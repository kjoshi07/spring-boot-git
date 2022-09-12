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
                    //def mvnHome = tool 'Maven 3.5.2'
                    if (isUnix()) {
                        def targetVersion = getDevVersion()
                        print 'target build version...'
                        print targetVersion
                        sh "mvn -Dintegration-tests.skip=true -Dbuild.number=${targetVersion} clean package"
                        def pom = readMavenPom file: 'pom.xml'
                        // get the current development version
                        developmentArtifactVersion = "${pom.version}-${targetVersion}"
                        print pom.version
                        // execute the unit testing and collect the reports
                        junit '**//*target/surefire-reports/TEST-*.xml'
                        archive 'target*//*.jar'
                    } else {
                        bat(/mvn -Dintegration-tests.skip=true clean package/)
                        def pom = readMavenPom file: 'pom.xml'
                        print pom.version
                        junit '**//*target/surefire-reports/TEST-*.xml'
                        archive 'target*//*.jar'
                    }
                }
            }
        }
        stage('Test') {
            steps {
                 script {
                     //def mvnHome = tool 'Maven 3.5.2'
                     if (isUnix()) {
                         // just to trigger the integration test without unit testing
                         sh "mvn  verify -Dunit-tests.skip=true"
                     } else {
                         bat(/mvn verify -Dunit-tests.skip=true/)
                     }

                 }
            }
        }
        stage('ECR Image Build & Push') {
            steps {
                script {
                    if (isUnix()) {
                        // just to trigger the integration test without unit testing
                        sh "aws ecr get-login-password --region us-west-1 | docker login --username AWS --password-stdin 278875135895.dkr.ecr.us-west-1.amazonaws.com"
                        sh "docker build -t kj007/git-repo ."
                        sh "docker tag kj007/git-repo:latest 278875135895.dkr.ecr.us-west-1.amazonaws.com/kj007/git-repo:latest"
                        sh "docker push 278875135895.dkr.ecr.us-west-1.amazonaws.com/kj007/git-repo:latest"
                    } else {
                        bat(/aws ecr get-login-password --region us-west-1 | docker login --username AWS --password-stdin 278875135895.dkr.ecr.us-west-1.amazonaws.com/)
                        bat(/docker build -t kj007/git-repo ./)
                        bat(/docker tag kj007/git-repo:latest 278875135895.dkr.ecr.us-west-1.amazonaws.com/kj007/git-repo:latest/)
                        bat(/docker push 278875135895.dkr.ecr.us-west-1.amazonaws.com/kj007/git-repo:latest/)
                    }
                }
            }
        }
    }
}