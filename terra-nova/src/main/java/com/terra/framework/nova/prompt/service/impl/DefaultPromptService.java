package com.terra.framework.nova.prompt.service.impl;

import com.terra.framework.nova.prompt.Prompt;
import com.terra.framework.nova.prompt.service.PromptService;
import com.terra.framework.nova.prompt.template.PromptTemplate;
import com.terra.framework.nova.prompt.template.PromptTemplateRegistry;

import java.util.Map;

/**
 * 提示词服务实现
 *
 * @author terra-nova
 */
public class DefaultPromptService implements PromptService {

    private final PromptTemplateRegistry registry;

    /**
     * 构造函数
     *
     * @param registry 模板注册表
     */
    public DefaultPromptService(PromptTemplateRegistry registry) {
        this.registry = registry;
    }

    @Override
    public String render(String templateId, Map<String, Object> variables) {
        PromptTemplate template = registry.getTemplate(templateId);
        return template.render(variables);
    }

    @Override
    public Prompt createPrompt(String templateId, Map<String, Object> variables) {
        PromptTemplate template = registry.getTemplate(templateId);
        return template.format(variables);
    }
}
