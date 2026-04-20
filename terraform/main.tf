terraform {
  required_version = ">= 1.0"
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 3.0"
    }
  }
}

provider "azurerm" {
  features {}
}

resource "azurerm_resource_group" "franchise" {
  name     = var.resource_group_name
  location = var.location
}

resource "azurerm_container_registry" "franchise" {
  name                = var.registry_name
  resource_group_name = azurerm_resource_group.franchise.name
  location            = azurerm_resource_group.franchise.location
  sku                 = "Standard"
  admin_enabled       = true
}

resource "azurerm_cosmosdb_account" "franchise" {
  name                = var.cosmos_account_name
  location            = azurerm_resource_group.franchise.location
  resource_group_name = azurerm_resource_group.franchise.name
  offer_type          = "Standard"
  kind                = "MongoDB"

  capabilities {
    name = "EnableMongo"
  }

  consistency_policy {
    consistency_level       = "Eventual"
    max_interval_in_seconds = 5
    max_staleness_prefix    = 100
  }

  geo_location {
    location          = azurerm_resource_group.franchise.location
    failover_priority = 0
  }
}

resource "azurerm_cosmosdb_mongo_database" "franchise_db" {
  name                = var.database_name
  resource_group_name = azurerm_resource_group.franchise.name
  account_name        = azurerm_cosmosdb_account.franchise.name
  throughput          = 400
}

resource "azurerm_app_service_plan" "franchise" {
  name                = var.app_service_plan_name
  location            = azurerm_resource_group.franchise.location
  resource_group_name = azurerm_resource_group.franchise.name
  kind                = "Linux"
  reserved            = true

  sku {
    tier = "Standard"
    size = "S1"
  }
}

resource "azurerm_app_service" "franchise" {
  name                = var.app_service_name
  location            = azurerm_resource_group.franchise.location
  resource_group_name = azurerm_resource_group.franchise.name
  app_service_plan_id = azurerm_app_service_plan.franchise.id

  site_config {
    linux_fx_version = "DOCKER|${azurerm_container_registry.franchise.login_server}/franchise-api:latest"
    always_on        = true
  }

  app_settings = {
    DOCKER_REGISTRY_SERVER_URL      = "https://${azurerm_container_registry.franchise.login_server}"
    DOCKER_REGISTRY_SERVER_USERNAME = azurerm_container_registry.franchise.admin_username
    DOCKER_REGISTRY_SERVER_PASSWORD = azurerm_container_registry.franchise.admin_password
    WEBSITES_ENABLE_APP_SERVICE_STORAGE = "false"

    SPRING_PROFILES_ACTIVE = "prod"
    SPRING_DATA_MONGODB_URI = azurerm_cosmosdb_account.franchise.connection_strings[0]
  }
}

resource "azurerm_application_insights" "franchise" {
  name                = var.app_insights_name
  location            = azurerm_resource_group.franchise.location
  resource_group_name = azurerm_resource_group.franchise.name
  application_type    = "java"
}
