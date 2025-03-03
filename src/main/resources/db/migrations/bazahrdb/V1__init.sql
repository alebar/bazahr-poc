create table membership (
  id serial primary key,
  created_at timestamp with time zone not null
);

create table inbox (
  id serial primary key,
  created_at timestamp with time zone not null,
  payload jsonb,
  status varchar
);