pipeline {
    agent any
    environment {
        AWS_ACCOUNT_ID="278875135895"
        AWS_DEFAULT_REGION="us-west-1"
        IMAGE_REPO_NAME="kj007/git-repo"
        IMAGE_TAG="latest"
        CLUSTER_NAME="MyFargateCluster"
        SERVICE_NAME="gir-service-stack-MyECSService-tr49dIcYiRTB"
        TASK_DEFINITION="gir-service-stack-MyTaskDefinition-tT4VsQlHzdkr"
        DESIRED_COUNT=1
        REPOSITORY_URI = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}"
    }

    stages {

        stage('Logging into AWS ECR') {
            steps {
                script {
                    sh "aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com"
                }

            }
        }

        stage('Build') {
            steps {
                script {
                    echo 'Pulling...' + env.BRANCH_NAME
                        sh "mvn -Dintegration-tests.skip=true clean package"
                        def pom = readMavenPom file: 'pom.xml'
                        print pom.version
                        // execute the unit testing and collect the reports
                        junit '**//*target/surefire-reports/TEST-*.xml'
                        archive 'target*//*.jar'
                    }
                }
            }
        stage('Test') {
            steps {
                script {
                        // just to trigger the integration test without unit testing
                        sh "mvn  verify -Dunit-tests.skip=true"
                    }

                }
            }

        // Building Docker images
        stage('Building image') {
            steps{
                script {
                    dockerImage = docker.build "${IMAGE_REPO_NAME}:${IMAGE_TAG}"
                }
            }
        }

        // Uploading Docker images into AWS ECR
        stage('Pushing to ECR') {
            steps{
                script {
                    sh "docker tag ${IMAGE_REPO_NAME}:${IMAGE_TAG} ${REPOSITORY_URI}:$IMAGE_TAG"
                    sh "docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}:${IMAGE_TAG}"
                }
            }
        }

        //Deploy to ECS Cluster.

        stage('Deploy to ECS') {
            steps{
                script {
                    sh "aws ecs update-service --cluster ${CLUSTER_NAME} --service ${SERVICE_NAME} --task-definition ${TASK_DEFINITION}:${DESIRED_COUNT} --desired-count ${DESIRED_COUNT}"
                }
            }
        }
    }
}