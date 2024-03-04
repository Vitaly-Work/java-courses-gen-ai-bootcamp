import boto3
import os
from dotenv import load_dotenv
from pathlib import Path

env_path = Path('../.env')
if not env_path.is_file():
    raise Exception('env file not found, check the current directory')

load_dotenv(dotenv_path=env_path)
s3_access_key = os.getenv('S3_ACCESS_KEY')
s3_secret_key = os.getenv('S3_SECRET_KEY')

s3 = boto3.client('s3',
                  endpoint_url='http://localhost:9000',
                  aws_access_key_id=s3_access_key,
                  aws_secret_access_key=s3_secret_key)

s3.create_bucket(Bucket='metrics-batch-input')
s3.create_bucket(Bucket='metrics-table')
