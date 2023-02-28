import * as sns from 'aws-cdk-lib/aws-sns';
import * as sqs from 'aws-cdk-lib/aws-sqs';
import { Construct } from "constructs";

const DOMAIN_TOPICS = ["ProfileCompleted"] as const
type Topic = typeof DOMAIN_TOPICS[number]

export type TopicsWithArn = {
    [k in Topic]: string
}


export class SNSTopicCreator {
    private stack: Construct
    private stackId: string
    private topics: Array<sns.Topic>
    private topicsWithArn: TopicsWithArn = {
        ProfileCompleted: "",
    }

    constructor(stack: Construct, stackId: string) {
        this.stack = stack
        this.stackId = stackId
    }

    createTopics(): SNSTopicCreator {
        this.topics = DOMAIN_TOPICS.map(topicName => {
            const topic = new sns.Topic(this.stack, `${topicName}`, {
                topicName: `${topicName}`,
                displayName: `${topicName}`
            });
            this.topicsWithArn[topicName] = topic.topicArn
            return topic
        })
        return this
    }

    subscribeQueueToTopics(queue: sqs.Queue, queueName: string): SNSTopicCreator {
        this.topics.forEach((topic, index) => {
            new sns.Subscription(this.stack, `${queueName}_${index}`, {
                protocol: sns.SubscriptionProtocol.SQS, endpoint: queue.queueArn, topic: topic
            })
        })
        return this
    }

    build(): TopicsWithArn {
        return this.topicsWithArn
    }
}
