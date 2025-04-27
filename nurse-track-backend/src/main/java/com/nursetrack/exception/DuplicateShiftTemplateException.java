package com.nursetrack.exception;

public class DuplicateShiftTemplateException extends RuntimeException
{
    public DuplicateShiftTemplateException(Long id, String name)
    {
        super(String.format("Ya existe una plantilla de turno con nombre '%s' en el departamento con ID %d", name, id));
    }
}
