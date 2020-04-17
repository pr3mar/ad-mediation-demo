#!/bin/bash
set -eo pipefail

# create instance
gcloud sql instances create ad-mediation-demo-db --tier=db-f1-micro --region=europe-west

# create database
gcloud sql databases create ad_mediation_db --instance=ad-mediation-demo-db
