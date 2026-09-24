ALTER TABLE public.tasks
    ADD CONSTRAINT tasks_user_id_positive_check CHECK (user_id > 0);