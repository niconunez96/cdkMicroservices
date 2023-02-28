import * as cdk from 'aws-cdk-lib';
import { AttributeType } from 'aws-cdk-lib/aws-dynamodb';
import * as ec2 from 'aws-cdk-lib/aws-ec2';
import * as ecr from 'aws-cdk-lib/aws-ecr';
import * as ecs from 'aws-cdk-lib/aws-ecs';
import * as ecs_patterns from 'aws-cdk-lib/aws-ecs-patterns';
import * as sqs from 'aws-cdk-lib/aws-sqs';
import { Construct } from 'constructs';
import { SNSTopicCreator } from './sns_topics';

export class CdkStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps & { localstack: boolean }) {
    super(scope, id, props);

    // const vpc = ec2.Vpc.fromLookup(this, 'vpc', { vpcId: "" });
//     const cluster = new ecs.Cluster(this, 'Cluster', { clusterName: `${id}_Cluster` });
//
//     const ecrRepo = new ecr.Repository(this, `${id}_checklist`, { imageTagMutability: ecr.TagMutability.MUTABLE })
//
//     const service = new ecs_patterns.ApplicationLoadBalancedFargateService(
//       this, `${id}_Fargate`, {
//       serviceName: `${id}_Service`,
//       cluster,
//       taskImageOptions: {
//         image: ecs.ContainerImage.fromEcrRepository(ecrRepo, "latest"),
//         containerPort: 8080,
//         containerName: "checklist_poc",
//       },
//       publicLoadBalancer: false,
//       desiredCount: 1,
//       listenerPort: 80,
//     });

    const organizationChecklistTable = new cdk.aws_dynamodb.Table(
      this,
      "OrganizationsChecklist",
      {
        tableName: "OrganizationsChecklist",
        partitionKey: {
          name: "organizationId",
          type: AttributeType.STRING
        }
      }
    )
//     organizationChecklistTable.grantReadWriteData(service.taskDefinition.taskRole)
    const domainEventsQueue = new sqs.Queue(this, "DomainEvents", {
      queueName: "DomainEvents",
    })
//     domainEventsQueue.grantConsumeMessages(service.taskDefinition.taskRole)
    const topicsWithArn = new SNSTopicCreator(this, id)
      .createTopics()
      .subscribeQueueToTopics(domainEventsQueue, "DomainEvents")
      .build()
  }
}
