# CI/CD

The GitHub Actions workflow in `.github/workflows/build.yml` does the following:

1. Builds, tests, and packages the WAR with Maven on pull requests and pushes.
2. Uploads the WAR as a GitHub Actions artifact.
3. On `main`, builds a Tomcat 10/JDK 17 container and publishes it to GHCR as `latest` and the commit SHA.
4. Optionally deploys that image to a Docker host over SSH.

## Required repository settings

- `SONAR_TOKEN`: optional SonarCloud token. The analysis job is skipped when absent.
- `GITHUB_TOKEN`: supplied automatically by GitHub Actions; package write permission is configured in the workflow.

## Optional server deployment

Add these repository secrets to enable the deploy job:

- `DEPLOY_HOST`: server hostname or IP.
- `DEPLOY_USER`: SSH user with Docker permission.
- `DEPLOY_SSH_KEY`: private SSH key for that user.

The target server must have Docker installed and allow SSH access. The application is published on port `8080` and uses the image from GHCR.