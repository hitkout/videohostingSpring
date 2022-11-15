#!/bin/bash

DOCKER_BUILDKIT=0 docker build -f ./Docker.baseimage -t acmecorp/baseimage:latest .

DOCKER_BUILDKIT=0 docker build -f ./Docker.server -t acmecorp/server:latest .

DOCKER_BUILDKIT=0 docker build -f ./Docker.logreader -t acmecorp/log-reader:latest .