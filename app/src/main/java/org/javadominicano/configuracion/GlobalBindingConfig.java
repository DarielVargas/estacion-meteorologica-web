package com.javadominicano.configuracion;

import java.beans.PropertyEditorSupport;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class GlobalBindingConfig {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        PropertyEditorSupport doubleEditor = new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.isBlank()) {
                    setValue(null);
                    return;
                }
                text = text.replace(',', '.');
                setValue(Double.parseDouble(text));
            }
        };

        binder.registerCustomEditor(Double.class, doubleEditor);
        binder.registerCustomEditor(double.class, doubleEditor);
    }
}
