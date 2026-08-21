---
apply: always
---

# Project Overview
This project is a Paper plugin for Minecraft version 1.21.11. It is inspired by the Origins mod and is designed to allow for flexible race creation. The philosophy behind this plugin differs significantly from Origins, which takes a fully data-driven approach. While this plugin does use a data-driven approach, it still relies primarily on a code-driven approach, as this is much simpler, more flexible, and more efficient.

# Security & Safety Guardrails
- Please never push changes on your own.

# Developing
- All constants must be defined as separate variables. Please avoid using hard-coded values in your code.
- Try not to nest conditions beyond the second depth, instead use Guard Cases.
- If a method becomes too large, it's a good idea to split it into separate helper methods to improve readability.
- Project separated in to 3 parts:
  1. API - the API itself. Should not contain any methods that other dev could access. 
  2. Core - the core of the plugin. Contains commands, global listeners, global runnables and some data-driven registries.
  3. Showcase - an example plugin that uses API.