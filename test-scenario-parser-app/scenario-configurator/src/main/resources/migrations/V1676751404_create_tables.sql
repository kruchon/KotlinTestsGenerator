create table project (
    id uuid primary key not null,
    name text not null,
    generation_package text not null,
    implementation_package text not null
);

create table scenario (
    id uuid primary key not null,
    name text not null,
    content text not null,
    project_id uuid not null references project(id)
);

create index scenario_project_idx on scenario(project_id);

create table source (
    id uuid primary key not null,
    name text not null,
    content text not null,
    project_id uuid not null references project(id)
);

create index source_project_idx on source(project_id);