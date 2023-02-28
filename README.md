## Requirements

```bash
brew install jq
npm install -g aws-cdk-local aws-cdk
# Install project dependencies
npm install
```

## Local development

### Create infrastructure

```bash
# Run localstack container
docker-compose up --build -d
# Deploy changes
LOCAL_INFRA=true cdklocal bootstrap
cdklocal deploy "LocalStack" --require-approval never --outputs-file /tmp/cdk_outputs.json && \
export API_URL=$(cat /tmp/cdk_outputs.json | jq -r '.LocalStack.apiUrl')
```

### Running tests

Before running tests you need

1. Running docker-compose containers
2. Setting up the cdk infrastructure

Now to run tests you must execute:


### Dynamodb Admin

Go to localhost:8002
