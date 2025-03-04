create table membership (
  id serial primary key,
  created_at timestamp with time zone not null
);

create table inbox (
  id serial primary key,
  created_at timestamp with time zone not null,
  processing_started_at timestamp with time zone,
  payload jsonb,
  status varchar
);