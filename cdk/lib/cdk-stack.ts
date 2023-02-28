import * as cdk from 'aws-cdk-lib';
import { AttributeType } from 'aws-cdk-lib/aws-dynamodb';
import * as sqs from 'aws-cdk-lib/aws-sqs';
import { Construct } from 'constructs';
import { SNSTopicCreator } from './sns_topics';

export class CdkStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps & { localstack: boolean }) {
    super(scope, id, props);

    const userChecklistsTable = new cdk.aws_dynamodb.Table(
      this,
      "UserChecklists",
      {
        tableName: "UserChecklists",
        partitionKey: {
          name: "id",
          type: AttributeType.STRING
        }
      }
    )
    const domainEventsQueue = new sqs.Queue(this, "DomainEvents", {
      queueName: "DomainEvents",
    })

    const DOMAIN_TOPICS = ["ProfileCompleted"] as const
    type Topic = typeof DOMAIN_TOPICS[number]

    const topicsWithArn = new SNSTopicCreator(this, id)
      .createTopics()
      .subscribeQueueToTopics(domainEventsQueue, "DomainEvents")
      .build()
  }
}
