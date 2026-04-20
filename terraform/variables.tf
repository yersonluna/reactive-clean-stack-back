variable "resource_group_name" {
  description = "Name of the Azure Resource Group"
  type        = string
  default     = "franchise-rg"
}

variable "location" {
  description = "Azure region for resources"
  type        = string
  default     = "eastus"
}

variable "registry_name" {
  description = "Name of the Container Registry"
  type        = string
  default     = "franchiseacr"
}

variable "cosmos_account_name" {
  description = "Name of the Cosmos DB account"
  type        = string
  default     = "franchise-cosmos"
}

variable "database_name" {
  description = "Name of the MongoDB database"
  type        = string
  default     = "franchise_db"
}

variable "app_service_plan_name" {
  description = "Name of the App Service Plan"
  type        = string
  default     = "franchise-plan"
}

variable "app_service_name" {
  description = "Name of the App Service"
  type        = string
  default     = "franchise-api-app"
}

variable "app_insights_name" {
  description = "Name of Application Insights"
  type        = string
  default     = "franchise-insights"
}
