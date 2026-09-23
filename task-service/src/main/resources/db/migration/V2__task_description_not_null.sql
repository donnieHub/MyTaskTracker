UPDATE public.tasks
SET description = ''
WHERE description IS NULL;

ALTER TABLE public.tasks
    ALTER COLUMN description SET DEFAULT '',
ALTER COLUMN description SET NOT NULL;