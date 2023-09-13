import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './attachment.reducer';

export const AttachmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const attachmentEntity = useAppSelector(state => state.attachment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="attachmentDetailsHeading">
          <Translate contentKey="odiseyDocApp.attachment.detail.title">Attachment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{attachmentEntity.id}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="odiseyDocApp.attachment.description">Description</Translate>
            </span>
          </dt>
          <dd>{attachmentEntity.description}</dd>
          <dt>
            <span id="file">
              <Translate contentKey="odiseyDocApp.attachment.file">File</Translate>
            </span>
          </dt>
          <dd>
            {attachmentEntity.file ? (
              <div>
                {attachmentEntity.fileContentType ? (
                  <a onClick={openFile(attachmentEntity.fileContentType, attachmentEntity.file)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {attachmentEntity.fileContentType}, {byteSize(attachmentEntity.file)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="odiseyDocApp.attachment.dodument">Dodument</Translate>
          </dt>
          <dd>{attachmentEntity.dodument ? attachmentEntity.dodument.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/attachment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/attachment/${attachmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AttachmentDetail;
