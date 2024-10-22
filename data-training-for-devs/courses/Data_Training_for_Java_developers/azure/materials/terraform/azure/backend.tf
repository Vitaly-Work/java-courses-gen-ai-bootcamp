terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "=4.6.0"
    }
    databricks = {
      source = "databricks/databricks"
      version = "1.54.0"
    }
    
    random = {
      source  = "hashicorp/random"
      version = "3.6.3"
    }
    
  }
}
