output "resource_group_name" {
  value = azurerm_resource_group.franchise.name
}

output "resource_group_id" {
  value = azurerm_resource_group.franchise.id
}

output "container_registry_login_server" {
  value = azurerm_container_registry.franchise.login_server
}

output "container_registry_id" {
  value = azurerm_container_registry.franchise.id
}

output "cosmosdb_endpoint" {
  value = azurerm_cosmosdb_account.franchise.endpoint
}

output "cosmosdb_connection_strings" {
  value     = azurerm_cosmosdb_account.franchise.connection_strings
  sensitive = true
}

output "app_service_url" {
  value = azurerm_app_service.franchise.default_site_hostname
}

output "app_service_id" {
  value = azurerm_app_service.franchise.id
}

output "app_insights_instrumentation_key" {
  value     = azurerm_application_insights.franchise.instrumentation_key
  sensitive = true
}
