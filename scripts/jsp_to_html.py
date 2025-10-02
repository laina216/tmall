#!/usr/bin/env python3
import re
import sys
from pathlib import Path
from typing import Dict, Set

ROOT = Path(__file__).resolve().parents[1]  # /workspace
SRC_DIR = ROOT / 'src' / 'main' / 'webapp'
OUT_DIR = ROOT / 'dist-html'

# Regex patterns
re_directive = re.compile(r"<%@[^%]*%>")
re_include = re.compile(r"<%@\s*include\s*file=\"([^\"]+)\"\s*%>")
# Note: EL expressions are left as-is except for contextPath handled in rewrite_paths()
re_c_tags = re.compile(r"</?c:[a-zA-Z]+(?:\s+[^>]*)?>")
re_fmt_tags = re.compile(r"</?fmt:[a-zA-Z]+(?:\s+[^>]*)?>")
re_jsp_tags = re.compile(r"</?jsp:[a-zA-Z]+(?:\s+[^>]*)?>")

# For simple c:if choose/when/otherwise removal but keep inner
re_c_open = re.compile(r"<c:(?:if|choose|when|otherwise)(?:\s+[^>]*)?>", re.IGNORECASE)
re_c_close = re.compile(r"</c:(?:if|choose|when|otherwise)>", re.IGNORECASE)

# Remove scriptlets and expressions <% ... %> and <%= ... %>
re_scriptlet = re.compile(r"<%[=!]?[\s\S]*?%>")

# Remove conditional class attributes with nested c:if inside attribute (best-effort)
re_attr_cif = re.compile(r"\s*<c:if[^>]*>([^<]*)</c:if>")


def read_text(path: Path) -> str:
    try:
        return path.read_text(encoding='utf-8')
    except Exception:
        return path.read_text(errors='ignore')


def resolve_include(base_file: Path, include_path: str) -> Path:
    # handle relative to base_file
    inc = (base_file.parent / include_path).resolve()
    return inc


def inline_includes(content: str, base_file: Path, seen: Set[Path]) -> str:
    def repl(match):
        inc_path = resolve_include(base_file, match.group(1))
        if inc_path in seen:
            return ''
        if not inc_path.exists():
            return ''
        seen.add(inc_path)
        inc_text = read_text(inc_path)
        inc_text = process_file_text(inc_text, inc_path, seen)
        return inc_text
    return re_include.sub(repl, content)


def strip_jsp_tags(content: str) -> str:
    # Remove taglib and page directives
    content = re_directive.sub('', content)
    # Remove JSTL container tags but keep inner content – handled below with broader approach
    content = re_c_open.sub('', content)
    content = re_c_close.sub('', content)
    # Remove general c:, fmt:, jsp: tags (self-closing or open/close); keep inner already handled above
    content = re_c_tags.sub('', content)
    content = re_fmt_tags.sub('', content)
    content = re_jsp_tags.sub('', content)
    # Remove any remaining scriptlets
    content = re_scriptlet.sub('', content)
    # Remove dangling attribute-level c:if fragments
    content = re_attr_cif.sub(r" \1", content)
    return content


def rewrite_paths(content: str) -> str:
    # Replace ${pageContext.request.contextPath} with /
    content = content.replace('${pageContext.request.contextPath}', '')
    # Clean double slashes except protocol
    content = re.sub(r'(?<!:)//+', '/', content)
    return content


def process_file_text(content: str, path: Path, seen: Set[Path]) -> str:
    # First inline includes recursively
    content = inline_includes(content, path, seen)
    # Then strip jsp/jstl
    content = strip_jsp_tags(content)
    # Rewrite paths
    content = rewrite_paths(content)
    return content


def convert_file(jsp_file: Path, out_root: Path):
    seen: Set[Path] = set([jsp_file])
    text = read_text(jsp_file)
    html = process_file_text(text, jsp_file, seen)
    rel = jsp_file.relative_to(SRC_DIR)
    out_path = out_root / rel
    out_path = out_path.with_suffix('.html')
    out_path.parent.mkdir(parents=True, exist_ok=True)
    out_path.write_text(html, encoding='utf-8')
    return out_path


def main():
    if not SRC_DIR.exists():
        print(f"Source dir not found: {SRC_DIR}", file=sys.stderr)
        sys.exit(1)
    OUT_DIR.mkdir(parents=True, exist_ok=True)
    jsp_files = list(SRC_DIR.rglob('*.jsp'))
    if not jsp_files:
        print('No JSP files found.', file=sys.stderr)
        sys.exit(1)
    print(f"Converting {len(jsp_files)} JSP files...")
    out_paths = []
    for jsp in jsp_files:
        out_paths.append(convert_file(jsp, OUT_DIR))
    print(f"Wrote {len(out_paths)} HTML files under {OUT_DIR}")


if __name__ == '__main__':
    main()
