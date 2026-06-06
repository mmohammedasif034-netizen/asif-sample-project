# Jenkins job and webhook setup

This folder contains a sample Jenkins job config (`job-config.xml`) you can import into Jenkins and instructions to set up a GitHub webhook.

Steps to create a Pipeline job in Jenkins:

1. In Jenkins, create a new item > Pipeline > give it a name.
2. Select "Pipeline script from SCM" and choose `Git`.
3. Set Repository URL to `https://github.com/mmohammedasif034-netizen/asif-sample-project.git` and Branch `main`.
4. Set `Script Path` to `Jenkinsfile` (already present in the repo).
5. Save and run the job.

To enable GitHub push-trigger (webhook):

1. In your Jenkins global configuration, install and configure the GitHub plugin if not already done.
2. In Jenkins, create or configure credentials (if using private repos) for Git access.
3. In GitHub repo Settings → Webhooks → Add webhook:
   - Payload URL: `https://<your-jenkins-host>/github-webhook/`
   - Content type: `application/json`
   - Which events: `Just the push event` (or choose `Send me everything`)
4. Save webhook. Push to the repository to verify Jenkins triggers.

Importing the provided `job-config.xml`:

1. Create a new Pipeline job, then click `Configure` → `Pipeline` → `Pipeline script from SCM`.
2. Alternatively, create a job folder and use `Jenkins Job DSL` or use `Jenkins CLI` to create the job from `job-config.xml`:

```bash
# Example using Jenkins CLI (requires CLI jar and authentication):
# java -jar jenkins-cli.jar -s https://<jenkins>/ create-job my-pipeline < jenkins/job-config.xml
```

Replace `<your-jenkins-host>` and credentials as appropriate.
