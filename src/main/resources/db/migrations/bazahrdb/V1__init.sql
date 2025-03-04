create table inbox (
  id serial primary key,
  type text not null,
  created_at timestamp with time zone not null,
  processing_started_at timestamp with time zone,
  payload jsonb,
  status varchar not null
);

create table membership (
  id serial primary key,
  created_at timestamp with time zone not null,
  person_id text,
  unit_id text
);

create table unit (
  id serial primary key,
  unit_id text not null,
  numerosity smallint
);

-- sample data for start
insert into unit (unit_id, numerosity) values ('urn:ekp:units/1a', 0);