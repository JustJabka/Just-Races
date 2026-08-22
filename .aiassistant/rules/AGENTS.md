---
apply: always
---

# Project Overview
This project is a Paper plugin for Minecraft version 1.21.11. It is inspired by the Origins mod and is designed to allow for flexible race creation. While it supports data-driven configuration, the primary focus is a code-driven approach for maximum performance, simplicity, and flexibility.

# Security & Safety Guardrails
- Do not push changes or execute git commands automatically. Always ask for confirmation.

# Code Style & Architecture
- Avoid hardcoded values. All constants must be defined as static final variables or config values.
- Max nesting depth for conditions is 2. Always prefer Guard Clauses (early returns) over deeply nested if-statements.
- Keep methods clean and focused. Refactor large methods into private helper methods.
- Project Architecture (3 modules):
  1. API - Contains public interfaces, events, and data models for external developers. NO internal implementation logic here.
  2. Core - Plugin core. Contains command execution, global listeners, tasks/runnables, and registries.
  3. Showcase - An example implementation/plugin demonstrating API usage.

# Context & Documentation
- When writing or reviewing code, always check the Paper API reference located in `@folder:sources` for correct method signatures, Adventure API components, and event types.