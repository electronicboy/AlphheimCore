alter table warps change `SERVER_NAME` WARP_SERVER varchar(32) null;

drop index IWARP_SERVER on warps;

create unique index IWARP_SERVER
	on warps (WARP_SERVER, WARP_NAME);