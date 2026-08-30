-- Bootstrap migrations for a fresh Smart R&D Agent database.
-- The source files stay versioned under docs/dev-ops/sql-migrations; this
-- manifest makes the local Docker database reproducible without running the
-- optional PostgreSQL migrations through a MySQL client.
SET NAMES utf8mb4;
USE `ai-agent-station-study`;

source /docker-entrypoint-initdb.d/migrations/V001__add_model_tier.sql;
source /docker-entrypoint-initdb.d/migrations/V002__create_chat_memory.sql;
source /docker-entrypoint-initdb.d/migrations/V003__create_chat_memory_summary.sql;
source /docker-entrypoint-initdb.d/migrations/V004__fix_chat_memory_comments_charset.sql;
source /docker-entrypoint-initdb.d/migrations/V005__add_multi_tier_models.sql;
source /docker-entrypoint-initdb.d/migrations/V006__create_long_term_memory.sql;
source /docker-entrypoint-initdb.d/migrations/V007__widen_conversation_id_for_multi_tenant.sql;
source /docker-entrypoint-initdb.d/migrations/V008__create_episodic_memory.sql;
source /docker-entrypoint-initdb.d/migrations/V009__create_audit_log.sql;
source /docker-entrypoint-initdb.d/migrations/V010__add_rag_file_hash.sql;
source /docker-entrypoint-initdb.d/migrations/V011__create_event_log.sql;
source /docker-entrypoint-initdb.d/migrations/V013__create_parent_document.sql;
source /docker-entrypoint-initdb.d/migrations/V014__add_parent_document_source.sql;

-- V015 used ADD COLUMN IF NOT EXISTS, which is not supported consistently by
-- MySQL 8.0. These are the same columns with fresh-database DDL.
ALTER TABLE ai_chat_memory ADD COLUMN user_id VARCHAR(64) DEFAULT NULL AFTER conversation_id;
ALTER TABLE ai_chat_memory_summary ADD COLUMN user_id VARCHAR(64) DEFAULT NULL AFTER conversation_id;
ALTER TABLE ai_client_rag_order ADD COLUMN user_id VARCHAR(64) DEFAULT NULL AFTER rag_id;
ALTER TABLE ai_parent_document ADD COLUMN user_id VARCHAR(64) DEFAULT NULL AFTER knowledge_tag;
ALTER TABLE ai_event_log ADD COLUMN user_id VARCHAR(64) DEFAULT NULL AFTER session_id;

source /docker-entrypoint-initdb.d/migrations/V016__ensure_ai_client_api.sql;
source /docker-entrypoint-initdb.d/migrations/V017__ensure_ai_client_advisor_4001.sql;
source /docker-entrypoint-initdb.d/migrations/V018__enable_fixed_agent_6.sql;
source /docker-entrypoint-initdb.d/migrations/V019__episodic_updated_at_and_rag_userid.sql;
source /docker-entrypoint-initdb.d/migrations/V020__episodic_last_summarized.sql;
source /docker-entrypoint-initdb.d/migrations/V021__alter_chat_memory_summary_add_watermark.sql;
source /docker-entrypoint-initdb.d/migrations/V022__add_pii_mask_advisor.sql;
source /docker-entrypoint-initdb.d/migrations/V025__add_flow_agent_2345.sql;
source /docker-entrypoint-initdb.d/migrations/V026__fix_flow_agent_2345_client_types.sql;
source /docker-entrypoint-initdb.d/migrations/V029__cleanup_agents.sql;
source /docker-entrypoint-initdb.d/migrations/V034__add_agent_id_to_chat_memory.sql;
source /docker-entrypoint-initdb.d/migrations/V041__create_mcp_tool_catalog.sql;
source /docker-entrypoint-initdb.d/migrations/V055__add_multimodal_chat_messages.sql;
source /docker-entrypoint-initdb.d/migrations/V058__create_background_task_center.sql;
source /docker-entrypoint-initdb.d/migrations/V059__create_eval_ops.sql;
source /docker-entrypoint-initdb.d/migrations/V060__create_eval_code_version_binding.sql;
