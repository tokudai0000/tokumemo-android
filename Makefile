
# Targets {{{

.PHONY: setup
setup:
	$(MAKE) setup-githooks

.PHONY: setup-githooks
setup-githooks:
	@if [ ! -d ".git/hooks" ]; then \
		mkdir -p .git/hooks; \
	fi
	@cp -r .githooks/* .git/hooks
	@chmod -R +x .git/hooks

# }}}
