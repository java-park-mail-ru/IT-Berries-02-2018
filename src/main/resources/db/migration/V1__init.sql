--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.12
-- Dumped by pg_dump version 9.5.12

-- Started on 2018-04-06 09:30:49 MSK

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2389 (class 1262 OID 16763)
-- Name: magical_dots_db; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE magical_dots_db WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'C' LC_CTYPE = 'C';


ALTER DATABASE magical_dots_db OWNER TO postgres;

\connect magical_dots_db

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12623)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2392 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 181 (class 1259 OID 16764)
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.flyway_schema_history OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 16776)
-- Name: history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.history (
    id_history bigint NOT NULL,
    date_result date,
    score integer,
    user_id bigint
);


ALTER TABLE public.history OWNER TO postgres;

--
-- TOC entry 182 (class 1259 OID 16774)
-- Name: history_id_history_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.history_id_history_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.history_id_history_seq OWNER TO postgres;

--
-- TOC entry 2393 (class 0 OID 0)
-- Dependencies: 182
-- Name: history_id_history_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.history_id_history_seq OWNED BY public.history.id_history;


--
-- TOC entry 185 (class 1259 OID 16784)
-- Name: users_; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_ (
    user_id bigint NOT NULL,
    avatar character varying(255),
    email character varying(255),
    password character varying(255),
    user_name character varying(255)
);


ALTER TABLE public.users_ OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 16782)
-- Name: users__user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users__user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users__user_id_seq OWNER TO postgres;

--
-- TOC entry 2394 (class 0 OID 0)
-- Dependencies: 184
-- Name: users__user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users__user_id_seq OWNED BY public.users_.user_id;


--
-- TOC entry 2260 (class 2604 OID 16779)
-- Name: id_history; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.history ALTER COLUMN id_history SET DEFAULT nextval('public.history_id_history_seq'::regclass);


--
-- TOC entry 2261 (class 2604 OID 16787)
-- Name: user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_ ALTER COLUMN user_id SET DEFAULT nextval('public.users__user_id_seq'::regclass);


--
-- TOC entry 2263 (class 2606 OID 16772)
-- Name: flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- TOC entry 2266 (class 2606 OID 16781)
-- Name: history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.history
    ADD CONSTRAINT history_pkey PRIMARY KEY (id_history);


--
-- TOC entry 2268 (class 2606 OID 16792)
-- Name: users__pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_
    ADD CONSTRAINT users__pkey PRIMARY KEY (user_id);


--
-- TOC entry 2264 (class 1259 OID 16773)
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- TOC entry 2269 (class 2606 OID 16793)
-- Name: fkde9rccaux4dxc0f5cgll1091d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.history
    ADD CONSTRAINT fkde9rccaux4dxc0f5cgll1091d FOREIGN KEY (user_id) REFERENCES public.users_(user_id);


--
-- TOC entry 2391 (class 0 OID 0)
-- Dependencies: 6
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2018-04-06 09:30:49 MSK

--
-- PostgreSQL database dump complete
--

